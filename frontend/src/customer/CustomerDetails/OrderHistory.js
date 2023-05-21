import React from 'react'
import ListContainer from '../../shared/util/ListContainer/ListContainer'

function OrderHistory() {
  const orders = [{
    "id": 1,
    "orderCode": "#O1235",
    "userId": "nekiuuid",
    "restaurantId": "nekiuuid",
    "orderStatus": "Delivered",
    "totalPrice": "52.5",
    "menuItems": [
      {
        "name": "Hamburger",
        "quantity": 3
      },
      {
        "name": "Palačinke Nutella",
        "quantity": 2
      },
      {
        "name": "Coca Cola 0.5",
        "quantity": 2
      },
      {
        "name": "Pepsi 0.5",
        "quantity": 1
      },
      {
        "name": "Pomfrit Srednji",
        "quantity": 3
      }
    ]
  },{
    "id": 2,
    "orderCode": "#O1235",
    "userId": "nekiuuid",
    "restaurantId": "nekiuuid",
    "orderStatus": "Delivered",
    "totalPrice": "52.5",
    "menuItems": [
      {
        "name": "Hamburger",
        "quantity": 3
      },
      {
        "name": "Palačinke Nutella",
        "quantity": 2
      },
      {
        "name": "Coca Cola 0.5",
        "quantity": 2
      },
      {
        "name": "Pepsi 0.5",
        "quantity": 1
      },
      {
        "name": "Pomfrit Srednji",
        "quantity": 3
      }
    ]
  },{
    "id": 3,
    "orderCode": "#O1235",
    "userId": "nekiuuid",
    "restaurantId": "nekiuuid",
    "orderStatus": "Delivered",
    "totalPrice": "52.5",
    "menuItems": [
      {
        "name": "Hamburger",
        "quantity": 3
      },
      {
        "name": "Palačinke Nutella",
        "quantity": 2
      }
    ]
  },{
    "id": 4,
    "orderCode": "#O1235",
    "userId": "nekiuuid",
    "restaurantId": "nekiuuid",
    "orderStatus": "Delivered",
    "totalPrice": "52.5",
    "menuItems": [
      {
        "name": "Hamburger",
        "quantity": 3
      },
      {
        "name": "Palačinke Nutella",
        "quantity": 2
      },
      {
        "name": "Coca Cola 0.5",
        "quantity": 2
      },
      {
        "name": "Pepsi 0.5",
        "quantity": 1
      },
      {
        "name": "Pomfrit Srednji",
        "quantity": 3
      }
    ]
  },{
    "id": 5,
    "orderCode": "#O1235",
    "userId": "nekiuuid",
    "restaurantId": "nekiuuid",
    "orderStatus": "Delivered",
    "totalPrice": "52.5",
    "menuItems": [
      {
        "name": "Hamburger",
        "quantity": 3
      },
      {
        "name": "Palačinke Nutella",
        "quantity": 2
      },
      {
        "name": "Coca Cola 0.5",
        "quantity": 2
      },
      {
        "name": "Pepsi 0.5",
        "quantity": 1
      },
      {
        "name": "Pomfrit Srednji",
        "quantity": 3
      }
    ]
  },{
    "id": 6,
    "orderCode": "#O1235",
    "userId": "nekiuuid",
    "restaurantId": "nekiuuid",
    "orderStatus": "Delivered",
    "totalPrice": "52.5",
    "menuItems": [
      {
        "name": "Hamburger",
        "quantity": 3
      },
      {
        "name": "Palačinke Nutella",
        "quantity": 2
      },
      {
        "name": "Coca Cola 0.5",
        "quantity": 2
      },
      {
        "name": "Pepsi 0.5",
        "quantity": 1
      },
      {
        "name": "Pomfrit Srednji",
        "quantity": 3
      }
    ]
  },{
    "id": 7,
    "orderCode": "#O1235",
    "userId": "nekiuuid",
    "restaurantId": "nekiuuid",
    "orderStatus": "Delivered",
    "totalPrice": "52.5",
    "menuItems": [
      {
        "name": "Hamburger",
        "quantity": 3
      },
      {
        "name": "Palačinke Nutella",
        "quantity": 2
      },
      {
        "name": "Coca Cola 0.5",
        "quantity": 2
      },
      {
        "name": "Pepsi 0.5",
        "quantity": 1
      },
      {
        "name": "Pomfrit Srednji",
        "quantity": 3
      }
    ]
  }]



  return (
    <ListContainer
      title={"My orders"}
      type="order"
      grid={false}
      items={orders}
      perPage={5}
    />
  )
}

export default OrderHistory