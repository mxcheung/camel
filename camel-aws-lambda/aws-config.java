import org.apache.camel.component.aws2.lambda.AWS2LambdaComponent;

AWS2LambdaComponent lambda = new AWS2LambdaComponent();
camelContext.addComponent("aws2-lambda", lambda);
