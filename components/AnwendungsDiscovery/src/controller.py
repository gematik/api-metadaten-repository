import os
import pwd
from fastapi import APIRouter, Response, File, UploadFile, HTTPException
from views import start_page
from jsonschema import validate, ValidationError
import json
from fastapi.responses import JSONResponse

router = APIRouter()

# Laden des JSON-Schemas
def load_schema(application):
    schema_path = os.path.join(os.getcwd(), 'schemas', application, 'schema.json')
    with open(schema_path, 'r') as schema_file:
        schema = json.load(schema_file)
        return schema

######Endpoints

# Startseite
@router.get("/")
def welcome_page():
    html_content = start_page()
    return Response(content=html_content, media_type='text/html')

# Event Listeners zum file-upload
# Sobald der Benutzer eine Datei auswählt, wird dieser Event ausgelöst und die darin enthaltene Funktion wird ausgeführt.
# PUT Provider
@router.post("/upload/{provider}/{application}/{version}")
async def upload_file(provider: str, application: str, version: str, file: UploadFile = File(...)):
    # Logik zum Erstellen des Ordners und Speichern der Datei
    contents = await file.read()

    # Laden der Schemen-Datei
    schema = load_schema(application)

    # Validierung der hochgeladenen Datei gegen das Schema
    try:
        uploaded_data = json.loads(contents)
        validate(instance=uploaded_data, schema=schema)

        # Pfad für den Upload-Ordner
        upload_folder_path = f"upload/{provider}/{application}/{version}"
        # Überprüfen, ob der Upload-Ordner vorhanden ist, und ihn erstellen, wenn nicht
        if not os.path.exists(upload_folder_path):
            os.makedirs(upload_folder_path)
            try:
                user_id = pwd.getpwnam('lars').pw_uid
                group_id = pwd.getpwnam('lars').pw_gid
            except KeyError as e:
                print(f"KeyError: {e}")
                return
            # Ändere die Berechtigungen des Ordners auf die des angegebenen Benutzers und der angegebenen Gruppe
            try:
                os.chown(upload_folder_path, user_id, group_id)
            except PermissionError as e:
                print(f"PermissionError: {e}")

        # Pfad zur hochgeladenen Datei
        file_name = application + ".json"
        file_path = os.path.join(upload_folder_path, file_name)
        # Die Datei speichern
        with open(file_path, "wb") as f:
            f.write(contents)

        return {"filename": file_name, "provider": provider}  # Erfolgreiche Antwort als JSON zurückgeben

    except json.JSONDecodeError as e:
        return JSONResponse(status_code=400, content="error")
    except ValidationError as e:
        return JSONResponse(status_code=400, content="error")
    except Exception as e:
        return JSONResponse(status_code=500, content="error")


# GET Consumer
@router.get("/services/{application}/{version}/{provider}")
async def get_schema(provider: str, application: str, version: str):
    schema_path = f"upload/{provider}/{application}/{version}/{application}.json"

    try:
        with open(schema_path, 'r') as schema_file:
            schema_data = json.load(schema_file)
        return JSONResponse(status_code=200, content=schema_data)
    except FileNotFoundError:
        return JSONResponse(status_code=404, content={"error": "Schema not found."})
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": f"Internal Server Error: {e}"})


# GET Schemen-Files
@router.get("/schemas/{application}")
def get_account_info(application: str):
    schema_path = f"schemas/{application}/schema.json"

    try:
        with open(schema_path, 'r') as schema_file:
            schema_data = json.load(schema_file)
        return JSONResponse(status_code=200, content=schema_data)
    except FileNotFoundError:
        return JSONResponse(status_code=404, content={"error": "Schema not found."})
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": f"Internal Server Error: {e}"})

