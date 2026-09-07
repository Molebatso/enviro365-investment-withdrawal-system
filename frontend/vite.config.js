import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Vite dev server runs on port 5173 by default, which matches
// the CORS allowlist configured in the backend's CorsConfig.java
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
  },
});
