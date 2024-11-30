import pyautogui
import datetime
import cv2


def capture():
    screenshot = pyautogui.screenshot()
    filename = f"images/image.png"
    screenshot.save(filename)
    image = cv2.imread(filename)
    height, width = image.shape[:2]
    new_width, new_height = width // 2, height // 2
    resized_image = cv2.resize(
        image, (new_width, new_height), interpolation=cv2.INTER_AREA
    )
    cv2.imwrite(filename, resized_image)
    return filename
