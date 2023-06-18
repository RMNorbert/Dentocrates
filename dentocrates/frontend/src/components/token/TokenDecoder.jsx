import jwt_decode from 'jwt-decode';

export const token = () => {
    try{
        return localStorage.getItem('token');
    } catch (error) {
        return null;
    }
}
  export const email = () => {
    try{
    const token = localStorage.getItem('token');
    const decodedToken = jwt_decode(token);
    return decodedToken.sub;
    } catch (error) {
        return null;
    }
}

export const role = () => {
    try {
        const token = localStorage.getItem('token');
        const decodedToken = jwt_decode(token);
        return decodedToken.role;
    } catch (error) {
          return null;
      }

}

export const userId = () => {
    try {
        return localStorage.getItem('userId');
    } catch (error) {
          return null;
      }

}
