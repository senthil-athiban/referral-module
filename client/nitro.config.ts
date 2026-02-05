import { defineNitroConfig } from 'nitro/config'

export default defineNitroConfig({
  routeRules: {
    '/api/**': {
      proxy: 'http://127.0.0.1:8080/api/**',
    },
  },
})
