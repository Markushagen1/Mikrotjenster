import axios from "axios";
import {jwtDecode} from "jwt-decode";

// Funksjon for å sjekke om token er gyldig
const isTokenValid = (token: string): boolean => {
    try {
        const decoded: { exp: number } = jwtDecode(token); // Dekod token og få utløpsdato
        const currentTime = Date.now() / 1000; // Nåværende tid i sekunder
        return decoded.exp > currentTime; // Sjekk om tokenet ikke er utløpt
    } catch (error) {
        console.error("Error decoding token:", error);
        return false;
    }
};

// Opprett Axios-klienten
const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Valider token ved opprettelse av klient
const token = localStorage.getItem("token");
if (token && isTokenValid(token)) {
    apiClient.defaults.headers['Authorization'] = `Bearer ${token}`;
} else {
    localStorage.removeItem("token"); // Fjern token hvis det er ugyldig eller utløpt
}

// Legg til en interceptor for å legge til Authorization-header dynamisk
apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token && isTokenValid(token)) {
        config.headers['Authorization'] = `Bearer ${token}`;
    } else if (token) {
        localStorage.removeItem("token"); // Fjern token hvis det er utløpt
    }
    return config;
}, (error) => {
    return Promise.reject(error); // Håndter forespørselsfeil
});

export default apiClient;
