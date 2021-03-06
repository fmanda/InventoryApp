<?php
	use Psr\Http\Message\ResponseInterface as Response;
	use Psr\Http\Message\ServerRequestInterface as Request;

	require '../vendor/autoload.php';
	require_once '../src/classes/DB.php';


	$app->get('/stock', function ($request, $response, $args) {
		try{
			$sql = "select * from v_stock";
	    $data = DB::openQuery($sql);
	    $json = json_encode($data);
	    $response->getBody()->write($json);

			return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
	    $msg = $e->getMessage();
	    $response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}
	});
