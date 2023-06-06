import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react'
import DialogTitle from '@mui/material/DialogTitle';
import { DialogContentText } from '@mui/material';
import DialogContent from '@mui/material/DialogContent';
import TextField from '@mui/material/TextField';
import authService from '../../service/auth.service';
import discountService from '../../service/discount.service';
import InputAdornment from '@mui/material/InputAdornment';
import Input from '@mui/material/Input';

export default function AddCoupon({open,setOpen,coupons,setCoupons}) {
   const user = authService.getCurrentUser();
   const [newCoupon, setnewCoupon] = useState({ code: "", discount_percentage: 0, quantity:0, restaurant_uuid:user.id })
   const [validation,setValidation] = useState(false)
   
 

  const handleClose = () => {
    setOpen(false);
  };
 

  const handleCreate = () => {
    setCoupons(oldArray => [...oldArray,newCoupon] );
    console.log(newCoupon)
    setOpen(false)
    discountService.addCoupon(newCoupon).then(res => {
        if (res.status == 201) {
            console.log(res.data)
        }
        else
            console.log(res)

    })
  }

  return (
    <div>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add coupon</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="normal"
            id="code"
            label="Coupon code"
            type="text"
            fullWidth
            variant="standard"
            helperText="The code needs to be 12 characters long!"
            error ={!validation}
            onChange={e => {
                if(e.target.value.length == 12) {
                    setValidation(true)
                    setnewCoupon({...newCoupon, ...{code:e.target.value}})
                }
                else setValidation(false)
            }}
          />

        <TextField
          label="Discount"
          variant="standard"
          id="discount"
          InputProps={{
            endAdornment: <InputAdornment position="end">%</InputAdornment>,
          }}
          onChange={e => {
            setnewCoupon({...newCoupon, ...{discount_percentage: e.target.value}})
        }}
        />
        <br></br>
          <TextField
            autoFocus
            endAdornment={<InputAdornment position="end">%</InputAdornment>}
            margin="dense"
            id="quantity"
            label="Quantity"
            type="number"
            variant="standard"
            onChange={e => {
                setnewCoupon({...newCoupon, ...{quantity: e.target.value}})
            }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleCreate}>Create</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}