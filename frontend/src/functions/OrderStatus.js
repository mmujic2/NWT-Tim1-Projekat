
export function orderStatus(status) {
    switch(status) {
        case "Accepted":
            return <span style={{color:"#FE724C",fontWeight:"bold"}}>Accepted</span>

        case "Ready for delivery":
            return <span style={{fontWeight:"bold"}} className="text-warning">Ready for delivery</span>

        case "Rejected":
            return <span style={{fontWeight:"bold"}} className="text-danger">Rejected</span>

        case "Cancelled":
            return <span style={{fontWeight:"bold"}} className="text-danger">Cancelled</span>

        case "Accepted for delivery":
            return <span style={{fontWeight:"bold"}} className="text-warning">Accepted for delivery</span>

        case "In delivery":
            return <span style={{fontWeight:"bold"}} className="text-info">In delivery</span>

        case "Delivered":
            return <span style={{fontWeight:"bold"}} className="text-success">Delivered</span>
    }
}