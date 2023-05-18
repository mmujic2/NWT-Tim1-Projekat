import "./App.css";

import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { Route,Routes } from "react-router-dom";
import RegisterPage from "./shared/RegisterPage/RegisterPage";

function App() {
 
  return(
    <>
    
      <Routes>
        <Route path="/register" element={<RegisterPage></RegisterPage>} />
        <Route path="/" element={<>hELLO</>} />
      </Routes>
      </>
  )
}

export default App;
