
import Modal from 'react-bootstrap/Modal';
import Map from './Map';

function MapModal({show,setShow,setAddress}) {
 
  const handleClose = () => setShow(false);
  
  return (
    <>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Enter your delivery address</Modal.Title>
        </Modal.Header>
        <Map setAddress={setAddress} ></Map>
      </Modal>
    </>
  );
}

export default MapModal;