import { invokeCall } from './authService';

const COMPANY_SERVICE_URL = `${process.env.REACT_APP_COMPANY_BASE_URL}`;
const env = `${process.env.REACT_APP_ENV}`;

export const getCompany = async (code) => {
    let url;
    if(env === 'local'){
        url = `${COMPANY_SERVICE_URL}/info/${code}`;
    } else {
        url = `${COMPANY_SERVICE_URL}/${code}`;
    }
    try {
        const response = await invokeCall(url);
        const data = await response.json();
        if (data.status === 200)
            return data;
    } catch (e) {
        console.log(e);
    }
    return { body : []};
}

export const getAllCompany = async () => {
    const url = `${COMPANY_SERVICE_URL}/getAll`;
    try {
        const response = await invokeCall(url);
        const data = await response.json();
        if (data.status === 200)
            return data;
    } catch (e) {
        console.log(e);
    }
    return [];
}