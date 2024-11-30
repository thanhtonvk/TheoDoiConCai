import sqlite3
import os
from datetime import datetime, timedelta
import shutil


def get_chrome_history():
    list_history = []
    list_downloads = []

    org_history = os.path.expanduser(
        r"~\AppData\Local\Google\Chrome\User Data\Default\History"
    )
    history_db = "db_history/temp_history.db"
    shutil.copy2(org_history, history_db)

    if not os.path.exists(history_db):
        print("Không tìm thấy file History.")
        return

    # Kết nối SQLite
    conn = sqlite3.connect(history_db)
    cursor = conn.cursor()

    # Lấy lịch sử duyệt web
    cursor.execute(
        """
        SELECT url, title, visit_count, last_visit_time
        FROM urls
        ORDER BY last_visit_time DESC
        LIMIT 100
    """
    )
    for row in cursor.fetchall():
        url, title, visit_count, last_visit_time = row
        timestamp = str(datetime(1601, 1, 1) + timedelta(microseconds=last_visit_time))
        list_history.append(
            {
                "url": url,
                "title": title,
                "visitCount": str(visit_count),
                "lastVisitTime": timestamp,
            }
        )

    cursor.execute(
        """
        SELECT target_path, total_bytes, start_time
        FROM downloads
        ORDER BY start_time DESC
        LIMIT 100
    """
    )
    for row in cursor.fetchall():
        target_path, total_bytes, start_time = row
        # start_time là số giây từ 1601-01-01 00:00:00 UTC
        timestamp = str(datetime(1601, 1, 1) + timedelta(microseconds=start_time))
        list_downloads.append(
            {"targetPath": target_path, "totalBytes": str(total_bytes), "time": timestamp}
        )

    # Đóng kết nối
    conn.close()
    return list_history, list_downloads
