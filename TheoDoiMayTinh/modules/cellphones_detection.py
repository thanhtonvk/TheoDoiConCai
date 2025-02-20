from ultralytics import YOLO
import cv2
from concurrent.futures import ThreadPoolExecutor
import pygame
import threading
import uuid
import time
from datetime import datetime


# Initialize pygame for audio playback
pygame.init()
pygame.mixer.init()

# Load model
model = YOLO("models/cellphone_s.pt")
executor = ThreadPoolExecutor(max_workers=1)

# Global variable to track last execution time
last_execution_time = 0
EXECUTION_DELAY = 10  # 10 seconds


def speak():
    pygame.mixer.music.load("alarm/khong_su_dung_dien_thoai.mp3")
    pygame.mixer.music.play()
    while pygame.mixer.music.get_busy():
        pygame.time.Clock().tick(20)


def send_image(image, bucket, db, id):
    file_path = "images/cellphone.png"
    height, width = image.shape[:2]
    new_width, new_height = width // 2, height // 2
    resized_image = cv2.resize(
        image, (new_width, new_height), interpolation=cv2.INTER_AREA
    )
    cv2.imwrite(file_path, resized_image)
    file_blob = uuid.uuid4().hex
    blob = bucket.blob(id + "/cellphone/" + file_blob + ".png")
    blob.upload_from_filename(file_path)
    blob.make_public()
    file_url = blob.public_url
    current_time = datetime.now()
    formatted_time = current_time.strftime("%Y-%m-%d %H:%M:%S")
    format_date = current_time.strftime("%Y-%m-%d")
    format_time = current_time.strftime("%H%M%S")
    data = {"time": formatted_time, "url": file_url, "id":format_time}
    db.reference(id).child("cellphone").child(format_date).child(format_time).set(data)


def predict_async(frame):
    return model.predict(frame, verbose=False, conf = 0.3)


def detect_cellphones(bucket, db, id):
    
    # nếu dùng camera ip thì để camera_source là đường dẫn, còn muốn dùng camera máy tính thì để là 0
    camera_source = "rtsp://admin:180683xo@192.168.1.2:554/onvif1"
    camera_source = 0
    cap = None
    
    if str(camera_source).isdigit():
        cap = cv2.VideoCapture(camera_source)
    else:
        import vlc
        instance = vlc.Instance()
        media = instance.media_new(camera_source)
        media_player = instance.media_player_new()
        media_player.set_media(media)
        media_player.play()
        snapshot_path = "snapshot1.png"
        media_player.video_take_snapshot(0, snapshot_path, 0, 0)
        time.sleep(2)
        
        
    global last_execution_time
    results_future = None
    results = []

    while True:
        if str(camera_source).isdigit():
            success, frame = cap.read()
            if not success:
                break
        else:
            media_player.video_take_snapshot(0, snapshot_path, 0, 0)
            frame = cv2.imread(snapshot_path)
            if frame is None:
                break


        if results_future is not None:
            results = results_future.result()
        results_future = executor.submit(predict_async, frame)

        detected = False
        for result in results:
            for box in result.boxes:
                x1, y1, x2, y2 = map(int, box.xyxy[0])
                conf = box.conf[0].item()
                cls = int(box.cls[0].item())
                if cls in [65, 33, 67, 38, 29]:
                    cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
                    cv2.putText(
                        frame,
                        f"Dien thoai",
                        (x1, y1 - 10),
                        cv2.FONT_HERSHEY_SIMPLEX,
                        0.5,
                        (0, 255, 0),
                        2,
                    )
                    detected = True

        current_time = time.time()
        if detected and (current_time - last_execution_time >= EXECUTION_DELAY):
            last_execution_time = current_time
            # threading.Thread(target=speak).start()
            threading.Thread(target=send_image, args=(frame, bucket, db, id)).start()

        cv2.imshow("Camera", frame)

        if cv2.waitKey(1) & 0xFF == ord("q"):
            break
    if cap is not None:
        cap.release()
    cv2.destroyAllWindows()
    executor.shutdown()
