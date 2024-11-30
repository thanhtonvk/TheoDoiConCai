import uuid


def get_id():
    computer_id = uuid.getnode()
    return str(computer_id)
