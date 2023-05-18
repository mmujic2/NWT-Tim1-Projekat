import React from "react";
import { Link } from "react-router-dom";
import Row from 'react-bootstrap/Row';
import {
  MDBBtn,
  MDBContainer,
  MDBRow,
  MDBCol,
  MDBCard,
  MDBCardBody,
  MDBInput,
  MDBIcon,
  MDBCheckbox,
} from "mdb-react-ui-kit";

import "./login.css";

import logo from "../../logo.png";

function Login({setPage}) {
  return (
    <>
    
    <MDBContainer fluid className="p-3 my-5 d-flex flex-column w-50">
      <MDBRow className="d-flex justify-content-center align-items-center h-100">
        <MDBCol col="12">
          <MDBCard
            className="bg-white my-5 mx-auto"
            style={{ borderRadius: "1rem", maxWidth: "1000px" }}
          >
            <MDBCardBody className="p-5 w-100 d-flex flex-column">
              <p className="text-white-50 mb-3">
                Please enter your username and password!
              </p>

              <MDBInput
                wrapperClass="mb-4 w-100"
                label="Email address"
                id="formControlLg"
                type="email"
                size="lg"
              />
              <MDBInput
                wrapperClass="mb-4 w-100"
                label="Password"
                id="formControlLg"
                type="password"
                size="lg"
              />

              <MDBCheckbox
                name="flexCheck"
                id="flexCheckDefault"
                className="mb-4"
                label="Remember password"
              />

              <MDBBtn size="lg">Login</MDBBtn>

              <hr className="my-4" />
              <Link onClick={()=>{setPage("signUp")}}>Create a new account</Link>

            </MDBCardBody>
          </MDBCard>
        </MDBCol>
      </MDBRow>
    </MDBContainer>
    </>
  );
}

export default Login;
