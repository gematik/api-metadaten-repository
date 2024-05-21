import os
from fastapi import Response

def start_page():
    file_path = os.path.join("ressources", "start_page.html")
    with open(file_path, "r") as f:
        html_content = f.read()
    return html_content