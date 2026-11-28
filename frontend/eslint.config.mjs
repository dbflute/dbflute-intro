import js from '@eslint/js'
import { defineConfig, globalIgnores } from 'eslint/config'
import prettier from 'eslint-config-prettier'
import html from 'eslint-plugin-html'
import jest from 'eslint-plugin-jest'
import globals from 'globals'
import tseslint from 'typescript-eslint'

export default defineConfig([
  globalIgnores(['dist/', 'src/static/app/api/intro/']),
  js.configs.recommended,
  tseslint.configs.recommended,
  prettier,
  {
    files: ['**/*.html', '**/*.riot'],
    plugins: { html },
    settings: {
      'html/html-extensions': ['.html', '.riot'],
    },
  },
  {
    plugins: { jest },
    languageOptions: {
      sourceType: 'module',
      globals: {
        ...globals.browser,
        ...globals.commonjs,
        ...globals.es2021,
        ...globals.jest,
        opts: true,
        observable: true,
        route: true,
      },
    },
    rules: {
      'no-console': ['error', { allow: ['warn', 'error'] }],
      'linebreak-style': ['error', 'unix'],
      'no-var': 'error',
      'prefer-arrow-callback': 'error',
      '@typescript-eslint/explicit-function-return-type': 'error',
    },
  },
  {
    files: ['__tests__/**'],
    rules: {
      '@typescript-eslint/no-explicit-any': 'off',
    },
  },
  {
    files: ['**/*.riot', '**/*.js'],
    rules: {
      '@typescript-eslint/explicit-function-return-type': 'off',
    },
  },
])
