import jwt_decode from 'jwt-decode';

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

export const id = () => {
    try {
        return localStorage.getItem('id');
    } catch (error) {
          return null;
      }

}
