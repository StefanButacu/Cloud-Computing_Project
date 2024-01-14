import axios from "axios";
import {baseURL} from "./index";
import {authURL} from "./index";


export const requestLogin = (username: string, password: string) => {
    return axios.post(authURL + `/auth/login`, {username: username, password: password})
}

