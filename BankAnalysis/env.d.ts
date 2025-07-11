/// <reference types="vite/client" />

declare module '*.vue' {
  import { ComponentOptions } from 'vue'
  const componentOptions: ComponentOptions
  export default componentOptions
}

// Extend existing ImportMetaEnv interface
interface ImportMetaEnv {
  readonly VITE_APP_TITLE?: string
  // Add more environment variables as needed
}
