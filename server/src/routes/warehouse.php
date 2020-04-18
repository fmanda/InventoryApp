<?php
	use Psr\Http\Message\ResponseInterface as Response;
	use Psr\Http\Message\ServerRequestInterface as Request;

	require '../vendor/autoload.php';
	require_once '../src/classes/DB.php';
	require_once '../src/models/ModelWarehouse.php';


	$app->get('/warehouse', function ($request, $response, $args) {
		try{
			$sql = "select * from warehouse";
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

	$app->post('/warehouse', function ($request, $response) {
		$json = $request->getBody();
		// $projectcode = $request->getAttribute('projectcode');
		$obj = json_decode($json);
		try{
			ModelWarehouse::saveToDB($obj);
	    $json = json_encode($obj);
	    $response->getBody()->write($json);
	    return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
			$msg = $e->getMessage();
	    $response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}
	});

	$app->delete('/warehouse/{id}', function ($request, $response) {		
		$id = $request->getAttribute('id');
		try{
			ModelWarehouse::deleteFromDB($id);
	    return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
			$msg = $e->getMessage();
	    $response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}

	});
