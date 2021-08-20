import jwt_decode from "jwt-decode";
const env = `${process.env.REACT_APP_ENV}`;

export const getAuthToken = async ()=> {
    const url = `${process.env.REACT_APP_AWS_TOKEN_URL}`;
    const headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': `Basic ${process.env.REACT_APP_AWS_CLIENT_SECRET}`
    }
    let body = new URLSearchParams({
        'grant_type': 'refresh_token',
        'client_id': `${process.env.REACT_APP_AWS_CLIENT_ID}`,
        'refresh_token': `${process.env.REACT_APP_AWS_REFRESH_TOKEN}`
    });
  try {
    let response = await fetch(url, {
        method: 'POST',
        headers: headers,
        body: body
    })
   let data = await response.json();
   return data;
      
  } catch (error) {
      console.log(error);
  }
    return {};
}

const validateToken = (token) => {
    if(!token){
        return false;
    }
    let decoded = jwt_decode(token);
    let exp = new Date(decoded.exp*1000);
    let curr = new Date();
    if(exp < curr){
        console.log('Token Expired');
        return false;
    }
    return true;
}

export const invokeCall = async (url) => {
    if(env === 'aws'){
        let token = localStorage.getItem('token');
        if(!validateToken(token)){
            console.log('Getting new token');
            getAuthToken().then(data=>{
                if(data && data.id_token){
                    let newToken = data['id_token']
                    localStorage.setItem('token', newToken);
                    token = newToken;
                }
            });
        }
        let headers = {
            'Authorization': token
        };
        return await fetch(url, {
            method:'GET',
            headers: headers
        });
    }
    return await fetch(url);
}
