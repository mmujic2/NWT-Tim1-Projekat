import React, { useEffect, useState } from 'react'
import ListContainer from '../../shared/util/ListContainer/ListContainer'
import orderService from '../../service/order.service'
import Loader from '../../shared/util/Loader/Loader'
import CustomAlert from '../../shared/util/Alert'


function OrderHistory() {
  const [orders, setOrders] = useState()
  const [loading, setLoading] = useState(true)
  const [alert, setAlert] = useState({})
  const [showAlert, setShowAlert] = useState(false)
  var mounted = false;

  useEffect(() => {
    if (!mounted) {
      mounted = true
      orderService.getUserOrders().then((res) => {
        setLoading(false)
        if (res.status == 200) {
          setOrders(res.data)
        } else {
          setAlert({ ...alert, msg: res.data.errors, type: "error" })
          setShowAlert(true)
        }
      })
    }
  }, [])



  return (
    <Loader isOpen={loading}>
      <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
      {orders ? <ListContainer
        title={"My orders"}
        type="order"
        grid={false}
        items={orders}
        perPage={5}
      /> : <></>}
    </Loader>
  )
}

export default OrderHistory