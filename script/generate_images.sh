#!/bin/sh -e
root_dir="$(git rev-parse --show-toplevel)"
ext_dir="$root_dir/ext"
exports_dir="$root_dir/exports"
mkdir -p "$ext_dir"

cli_version="1.10.1"
cli_zip="$ext_dir/cli-${cli_version}.zip"
if [ ! -f "$cli_zip" ]; then
  echo
  echo "🔧 Downloading Structurizr CLI..."
  wget "https://github.com/structurizr/cli/releases/download/v$cli_version/structurizr-cli-$cli_version.zip" -O"$cli_zip"
  unzip -o -d"$ext_dir" "$cli_zip"
fi

plantuml_version="1.2021.7"
plantuml_jar="$ext_dir/plantuml-nodot.${plantuml_version}.jar"
if [ ! -f "$plantuml_jar" ]; then
  echo
  echo "🔧 Downloading PlantUML..."
  wget "http://sourceforge.net/projects/plantuml/files/plantuml-nodot.${plantuml_version}.jar/download" -O"$plantuml_jar"
fi

echo
echo "🚧 Generating Structurizr workspaces..."
"$root_dir/gradlew" run

echo
echo "🌿 Generating PlantUML..."
(cd "$exports_dir"; \
  find "$root_dir" -name 'structurizr-*-local.json' \
    -exec "$ext_dir/structurizr.sh" export -workspace {} -f plantuml \;)

echo
echo "🖼 Generating images..."
(cd "$exports_dir"; \
  java -Djava.awt.headless=true -jar "$plantuml_jar" -SmaxMessageSize=100 -tpng structurizr-*.puml)
