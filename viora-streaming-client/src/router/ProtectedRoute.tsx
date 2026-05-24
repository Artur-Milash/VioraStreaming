import {Navigate} from 'react-router-dom';
import {API_PAGE} from "../constants/routingConstants.ts";
import {AppLayout} from "../components/Layout/AppLayout.tsx";
import {getToken} from "../utils/apiUtils.ts";

const ProtectedRoute = () => {
  const token = getToken();

  if (!token) {
    return <Navigate to={API_PAGE.Auth} replace/>
  }

  return (
      <AppLayout/>
  )
};

export default ProtectedRoute;