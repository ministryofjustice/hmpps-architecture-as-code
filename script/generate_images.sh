#!/bin/sh -e
root_dir="$(git rev-parse --show-toplevel)"
ext_dir="$root_dir/ext"
exports_dir="$root_dir/exports"
mkdir -p "$ext_dir"

cli_version="2024.01.02"
cli_zip="$ext_dir/structurizr-cli.zip"
if [ ! -f "$cli_zip" ]; then
  echo
  echo "🔧 Downloading Structurizr CLI..."
  wget "https://github.com/structurizr/cli/releases/download/$cli_version/structurizr-cli.zip" -P ext
  unzip -o -d"$ext_dir" "$cli_zip"
fi

plantuml_version="1.2023.1"
plantuml_jar="$ext_dir/plantuml-nodot.${plantuml_version}.jar"
if [ ! -f "$plantuml_jar" ]; then
  echo
  echo "🔧 Downloading PlantUML..."
  wget "http://sourceforge.net/projects/plantuml/files/${plantuml_version}/plantuml-nodot.${plantuml_version}.jar/download" -O"$plantuml_jar"
fi

echo
echo "🚧 Generating Structurizr workspaces..."
"$root_dir/gradlew" run

echo
echo "🌿 Generating PlantUML..."
(
  cd "$exports_dir"
  find "$root_dir" -name 'structurizr-*-local.json' \
    -exec "$ext_dir/structurizr.sh" export -workspace {} -f plantuml \;
)

echo
echo "🖼 Generating images..."
(
  cd "$exports_dir"
  java -Djava.awt.headless=true -jar "$plantuml_jar" -SmaxMessageSize=100 -tpng structurizr-*.puml
)
