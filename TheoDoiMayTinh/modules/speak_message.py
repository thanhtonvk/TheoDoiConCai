from gtts import gTTS
import pygame
import uuid

pygame.init()
pygame.mixer.init()


def speak(text):
    
    tts = gTTS(text=text, lang="vi")
    file_name = f"sounds/{uuid.uuid4().hex}.mpg"
    tts.save(file_name)
    pygame.mixer.music.load(file_name)
    pygame.mixer.music.play()
    while pygame.mixer.music.get_busy(): 
        pygame.time.Clock().tick(10)
