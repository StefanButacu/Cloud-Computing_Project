export const authConfig = (token?: string) => ({
    headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
    }
})
// require('dotenv').config();

export const baseURL = process.env.REACT_APP_SERVER_URL;
// export const baseURL = "http://192.168.93.57:8080";

console.log(baseURL);