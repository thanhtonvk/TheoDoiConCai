from ultralytics import YOLO
import cv2
from concurrent.futures import ThreadPoolExecutor
import pygame
import threading
import uuid
import time

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
    db.reference(id).child("cellphone").set(file_url)

def predict_async(frame):
    return model.predict(frame, verbose=False)

def detect_cellphones(bucket, db, id):
    global last_execution_time
    cap = cv2.VideoCapture(0)
    count = 0
    results_future = None
    results = []

    while cap.isOpened():
        success, frame = cap.read()
        count += 1
        if not success:
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
            threading.Thread(target=speak).start()
            threading.Thread(
                target=send_image, args=(frame, bucket, db, id)
            ).start()

        cv2.imshow("YOLOv8 Real-time Detection", frame)

        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    cap.release()
    cv2.destroyAllWindows()
    executor.shutdown()
