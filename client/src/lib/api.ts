import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1', // Adjust if needed
})

export default api
