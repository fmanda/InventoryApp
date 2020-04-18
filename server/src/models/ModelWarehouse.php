<?php
	require_once '../src/models/BaseModel.php';

	class ModelWarehouse extends BaseModel{
		public static function getFields(){
			return array(
				"warehousename"
			);
		}

		public static function saveToDB($obj){
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				static::saveObjToDB($obj, $db);
				$db->commit();
				$db = null;
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function deleteFromDB($id){
			try {
				$str = static::generateSQLDelete("id=". $id);
				DB::executeSQL($str);
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function retrieve($id){
			$obj = parent::retrieve($id);
			// if (isset($obj->area_id)){
			// 	$obj->area =  ModelArea::retrieve($obj->area_id);
			// }
			return $obj;
		}


	}
