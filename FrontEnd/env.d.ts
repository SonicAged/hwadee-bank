/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// Extend existing ImportMetaEnv interface
interface ImportMetaEnv {
  readonly VITE_APP_TITLE?: string
  // Add more environment variables as needed
}
