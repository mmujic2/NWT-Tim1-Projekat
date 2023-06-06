import React, { useState, useEffect } from "react";
import Loader from "../../shared/util/Loader/Loader";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import discountService from "../../service/discount.service";

function CouponList() {
    const [loading, setLoading] = useState(true);
    const [coupons, setCoupons] = useState();

    useEffect(() => {
        discountService.getAllCoupons().then((res) => {
          if (res.status == 200) {
            setCoupons(res.data);
            setLoading(false);
            console.log(res.data);
          }
        });
    }, []);

    return (
        <div>
        <Loader isOpen={loading}>
            { coupons ? <ListContainer
                  title={"Active coupons"}
                  type="coupon"
                  grid={false}
                  items={coupons}
                  setItems={setCoupons}
                  perPage={5}
                /> : <></>
            }
        </Loader>
        </div>
      );


}

export default CouponList