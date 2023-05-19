import React, { useEffect } from "react";
import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
import styles from "./login.css";
import LocationOn from "@mui/icons-material/LocationOn";
import { useState } from "react";
import Button from "react-bootstrap/Button";
import { Link, useNavigate } from "react-router-dom";
import auth from "../../../service/auth.service";
import CustomAlert from "../../util/Alert";
import MapModal from "../../MapModal/MapModal";
import { Col, Row } from "react-bootstrap";
import SockJsClient from 'react-stomp';

function Login({ setPage }) {

  return (
    <>
      <Container className={styles.container}>
        <Form>
          <h1>Login</h1>

          <Form.Group className="mb-3" controlId="formGroupUsername">
            <Form.Label>Username</Form.Label>
            <Form.Control
              required
              type="text"
              name="username"
              //   value={formData.username}
              //  onChange={handleChange}
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId="formGroupPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              required
              type="password"
              name="password"
              //  value={formData.password}
              // onChange={handleChange}
            />
          </Form.Group>

          <Button className={styles.btn} type="submit">
            Login
          </Button>
          <hr></hr>
          <div style={{ textAlign: "center" }}>
            Don't have an account yet?
            <Link
              className="px-3 lnk"
              onClick={() => {
                setPage("signup");
              }}
            >
              Sign up.
            </Link>
          </div>
        </Form>
      </Container>
      <SockJsClient url="http://localhost:7070/websocket" 
      topics={['/message/test']} 
      onMessage={(msg) => {console.log(msg)}}></SockJsClient>
      Test
    </>
  );
}

export default Login;
