export const authConfig = (token?: string) => ({
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
    }
})
export const baseURL = "http://localhost:8080";
// export const baseURL = "http://192.168.93.57:8080";
