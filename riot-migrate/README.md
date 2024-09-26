# Riot Migrate

## Overview
Riot Migrate is a tool designed to facilitate the migration of Riot.js v3 components to Riot.js v4 and later versions.

## Usage
Example usage:
```sh
npm run migrate -- \
  --target ./frontend/src/static/app/pages/client/replace-schema/ex-replace-schema.tag \
  --output ./frontend/src/static/app/pages/client/replace-schema/test-replace-schema.ts \
  --name replace-schema
```

### Command Line Options
- `--target` (`-t`): Path to the Riot.js v3 tag file to be migrated
- `--output` (`-o`): Destination directory and filename for the migrated component
- `--name` (`-n`): Name of the component to be migrated (optional)

## Description
Riot Migrate automates the process of upgrading Riot.js components from version 3 to version 4 or later. It analyzes the structure and syntax of v3 components and transforms them into the format compatible with newer Riot.js versions.

## Features
- Converts Riot.js v3 tag files to TypeScript-based Riot.js v4+ components
- Preserves component logic and structure
- Handles lifecycle methods and event bindings
- Generates TypeScript interfaces for props and state

## Requirements
- Node.js (version X.X.X or later)
- npm (version X.X.X or later)

## Installation
TBD

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## License
TBD

## Support
If you encounter any problems or have any questions, please open an issue in the GitHub repository.
```

Note: You may want to add more sections or details depending on the complexity of your tool and any additional information you want to provide to users. Also, remember to replace placeholder text (like version numbers in the Requirements section) with actual information relevant to your project.
