import React from "react";
import { Card, Col, Row } from "react-bootstrap";
import StarRatings from "react-star-ratings";

function ReviewCard({ review, grid }) {
  return (
    <Card
      style={{
        width: "100%",
        height: "fit-content",
        overflow: "hidden",
        backgroundColor: "#D9D9D9",
      }}
      className="box"
    >
      <Card.Body>
        <Row>
          <div
            style={{
              position: "absolute",
              top: 10,
              left: 15,
              display: "inline",
            }}
          >
            <StarRatings
              rating={review.rating}
              starRatedColor="#FE724C"
              numberOfStars={5}
              name="rating"
              starDimension="30px"
              starSpacing="2px"
              starEmptyColor="white"
            />
            <span
              style={{
                color: "#FE724C",
                verticalAlign: "middle",
                fontSize: "22px",
                fontWeight: "bold",
              }}
            >
              {" "}
              {review.rating}
            </span>
          </div>
        </Row>
        {review.comment ? 
        <Row style={{marginTop:"20px"}}>
          <span style={{color:"#272D2F"}}>{review.comment}</span>
        </Row> : <></>}
      </Card.Body>
    </Card>
  );
}

export default ReviewCard;
