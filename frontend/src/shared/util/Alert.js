import Toast from 'react-bootstrap/Toast'
import { ToastContainer } from 'react-bootstrap';
import { CheckCircleFill,XCircleFill,InfoCircleFill } from 'react-bootstrap-icons';


function CustomAlert({ type, msg=[], show, setShow }) {
    const variant = type=="error"? 'danger' : type=="success" ? "success" : "info"
    //Possible variants: primary, secondary, success, danger, warning, info, light, dark
    return (
        <>
            <ToastContainer className="p-3" position={'top-end'} >
                <Toast onClose={() => setShow(false)} animation show={show} bg={variant} delay={10000} autohide>
                    <Toast.Header>
                        {type =="error"?
                            <>
                            <XCircleFill className="text-danger" size={20}></XCircleFill>
                            <strong className="me-auto px-2 text-danger">Error</strong>
                            </>
                         : type== "success" ?
                         <>
                         <CheckCircleFill className="text-success" size={20}/>
                         <strong className="me-auto px-2 text-success" color='red'>Success</strong>
                         </>
                         :  <>
                         <InfoCircleFill className="text-info" size={20}/>
                         <strong className="me-auto px-2 text-info">Info</strong>
                         </>
                         }
                        
                    </Toast.Header>

                    <Toast.Body className='text-white'>
                        <div>
                       {msg.map(i => <span>{i}<br></br></span>)}
                       </div>
                    </Toast.Body>
                </Toast>
            </ToastContainer >

        </>
    );
}

export default CustomAlert;