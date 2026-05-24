import {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {API_PAGE} from "../constants/routingConstants.ts";
import {getToken, removeToken} from "../utils/apiUtils.ts";

const isJwtExpired = (token: string): boolean => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.exp * 1000 < Date.now();
  } catch {
    return true;
  }
};

const useLogin = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = getToken();

    if (!token || isJwtExpired(token)) {
      removeToken();
      navigate(API_PAGE.Auth, {replace: true});
    }
  }, [navigate]);
};

export default useLogin;