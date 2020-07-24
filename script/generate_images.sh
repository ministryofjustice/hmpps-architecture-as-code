#!/bin/sh -e
root_dir="$(git rev-parse --show-toplevel)"
ext_dir="$root_dir/ext"
exports_dir="$root_dir/exports"
cli_version="1.2.0"
cli_zip="$ext_dir/cli.zip"

mkdir -p "$ext_dir"
if [ ! -f "$cli_zip" ]; then
  echo
  echo "🔧 Downloading Structurizr CLI..."
  wget "https://github.com/structurizr/cli/releases/download/v$cli_version/structurizr-cli-$cli_version.zip" -O"$cli_zip"
  unzip -d"$ext_dir" "$cli_zip"
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
  plantuml -SmaxMessageSize=100 -tpng structurizr-*.puml)
