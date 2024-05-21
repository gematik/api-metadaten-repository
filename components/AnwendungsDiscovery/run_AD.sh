#!/bin/bash

# Verzeichnisname
directory="datastore"

# Überprüfen und Installieren von fehlenden Python-Paketen
install_package() {
    package_name=$1
    if ! python -c "import $package_name" &>/dev/null; then
        echo "Installing $package_name..."
        pip install -q $package_name  # -q oder --quiet, um die Ausgabe zu unterdrücken
    fi
}

# Installation aller Pakete aus der python_libs.txt
echo "Installing required Python packages..."
while IFS= read -r line; do
    # Ignorieren von Kommentaren und leeren Zeilen
    if [[ ! $line =~ ^\s*# && -n $line ]]; then
        install_package "$line"
    fi
done < python_libs.txt

# Ermittlung des aktuellen Verzeichnisses des Skripts
project_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Hinzufügen des src-Verzeichnisses zum PYTHONPATH
export PYTHONPATH="$project_dir/src:$PYTHONPATH"

# Prüfen, ob es den Ordner datastore gibt
if [ ! -d "$directory" ]; then
    # Wenn das Verzeichnis nicht existiert, erstellen Sie es
    mkdir -p "$directory"
    echo "Verzeichnis erstellt: $directory"
else
    echo "Verzeichnis existiert bereits: $directory"
fi

# Starten der Anwendungs-Discovery
python src/main.py