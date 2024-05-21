FROM python:3.9.7

WORKDIR /app

# Installiere die erforderlichen Pakete, einschließlich libssl-dev
#RUN apt-get update && \
#    apt-get install -y libssl-dev#

# Erstelle den Ordnerstruktur
RUN mkdir examples ressources schemas src upload

# Kopiereden Inhalt in den Container
COPY examples/ /app/examples/
COPY ressources/ /app/ressources/
COPY schemas/ /app/schemas/
COPY src/ /app/src/
COPY upload/ /app/upload/

COPY python_libs.txt .

# Installiere die Python-Abhängigkeiten
RUN pip install --upgrade pip
RUN pip install -r python_libs.txt

# Starte die Anwendung
CMD ["python", "src/main.py"]