import React from 'react'
import { Carousel } from 'react-bootstrap';
import default_img from "../../images/output-onlinejpgtools.jpg"

function Gallery() {
  return (
    <Carousel fade>
    <Carousel.Item interval={10000}>
        <img
            style={{ height: 300 }}
            className="d-block w-100"
            src={default_img}
            alt="First slide"
        />

    </Carousel.Item>
    <Carousel.Item interval={10000}>
        <img
            style={{ height: 300 }}
            className="d-block w-100"
            src={default_img}
            alt="Second slide"
        />
        <Carousel.Caption>
            <h3>Second slide label</h3>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
        </Carousel.Caption>
    </Carousel.Item>
    <Carousel.Item interval={10000}>
        <img
            style={{ height: 300 }}
            className="d-block w-100 "
            src={default_img}
            alt="Third slide"
        />
        <Carousel.Caption>
            <h3>Third slide label</h3>
            <p>
                Praesent commodo cursus magna, vel scelerisque nisl consectetur.
            </p>
        </Carousel.Caption>
    </Carousel.Item>
</Carousel>
  )
}

export default Gallery