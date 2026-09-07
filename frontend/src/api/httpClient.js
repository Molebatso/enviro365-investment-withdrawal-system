import axios from 'axios';

// Single Axios instance shared by all API modules, pointed at the
// Spring Boot backend. Centralizing this means the base URL only
// needs to change in one place if the backend port/host changes.
const httpClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default httpClient;
