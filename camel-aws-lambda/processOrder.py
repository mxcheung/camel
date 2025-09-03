import json
from datetime import datetime

def lambda_handler(event, context):
    """
    This function is triggered by Camel via aws2-lambda.
    `event` will be the JSON payload Camel sent.
    """
    print("Received event:", json.dumps(event))

    # Example: Extract order event
    order_event = event.get("orderEvent", {})

    # Add processing metadata
    result = {
        "orderId": order_event.get("orderId"),
        "processed": True,
        "timestamp": datetime.utcnow().isoformat() + "Z"
    }

    # Return result back to Camel
    return result
