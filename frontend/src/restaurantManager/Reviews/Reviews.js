import React, { useEffect } from 'react'
import { useState } from 'react'
import Loader from "../../shared/util/Loader/Loader"
import ListContainer from "../../shared/util/ListContainer/ListContainer"
import restaurantService from '../../service/restaurant.service';

function Reviews() {
    const [reviews, setReviews] = useState();
    const [loading,setLoading] = useState(false);

    var mounted = false;
    useEffect(()=> {
        if(!mounted) {
            mounted=true;
            setLoading(true)
            restaurantService.getReviews().then((res)=> {
                setLoading(false)
                if(res.status==200) {
                    setReviews(res.data)
                }
            })
        }
    },[])
  return (
    <Loader isOpen={loading}>
        {reviews? 
        <ListContainer
        title="Reviews"
        setItems={reviews}
        items={reviews}
        perPage={8}
        grid={false}
        type="review"
        ></ListContainer>
        : <></>}
    </Loader>
  )
}

export default Reviews