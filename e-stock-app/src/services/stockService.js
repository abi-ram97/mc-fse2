import { invokeCall } from './authService';

const STOCK_URL = `${process.env.REACT_APP_STOCK_BASE_URL}`;

export const getStockByInterval = async (companyCode, searchCriteria) => {
    const url = `${STOCK_URL}/get/${companyCode}/${searchCriteria.startDate}/${searchCriteria.endDate}`;
    try{
        const response = await invokeCall(url);
        const data = await response.json();
        console.log(data);
        if(data.status === 200)
            return data;
    } catch(e){
        console.log(e);
    }
    return [];
}

export const getAllStocks = async (companyCode) => {
    const url = `${STOCK_URL}/${companyCode}`;
    try{
        const response = await invokeCall(url);
        const data = await response.json();
        if(data.status === 200)
            return data;
    } catch(e){
        console.log(e);
    }
    return [];
}