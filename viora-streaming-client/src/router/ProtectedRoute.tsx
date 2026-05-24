import {Navigate} from 'react-router-dom';
import {API_PAGE} from "../constants/routingConstants.ts";
import {AppLayout} from "../components/Layout/AppLayout.tsx";
import {getToken} from "../utils/apiUtils.ts";
import type {ReactNode} from "react";

interface ProtectedRouteProps {
  children?: ReactNode;
}

const ProtectedRoute = ({children}: ProtectedRouteProps) => {
  const token = getToken();

  if (!token) {
    return <Navigate to={API_PAGE.Auth} replace/>
  }

  return children ? <>{children}</> : <AppLayout/>;
};

export default ProtectedRoute;