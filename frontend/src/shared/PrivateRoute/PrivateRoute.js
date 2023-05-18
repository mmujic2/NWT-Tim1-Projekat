import React from "react";
import { useLocalState } from "../../util/useLocalState";
import {Navigate,Outlet} from "react-router-dom";
import { Buffer } from "buffer";
import AuthService from "../../util/auth.service";

const PrivateRoute = (props)=> {
   var user = AuthService.getCurrentUser();

    return (
        user ? <Outlet props={props}/>: <Navigate to='/register'></Navigate>
    )

   
}

export default PrivateRoute;