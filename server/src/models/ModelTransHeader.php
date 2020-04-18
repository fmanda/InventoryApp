<?php
	require_once '../src/models/BaseModel.php';

	class ModelTransHeader extends BaseModel{
		public static function getFields(){
			return array(
				"transno", "transdate", "header_flag", "warehouse_id", "notes"
			);
		}

		public static function saveToDB($obj){
			$db = new DB();
			$db = $db->connect();
			$db->beginTransaction();
			try {
				if (!static::isNewTransaction($obj)){
					$sql = ModelTransDetail::generateSQLDelete('header_id = '. $obj->id);
					$db->prepare($sql)->execute();
				}

				static::saveObjToDB($obj, $db);
				foreach($obj->items as $item){
					$item->header_id = $obj->id;
					ModelTransDetail::saveToDB($item, $db);
				}
				$db->commit();
				$db = null;
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function deleteFromDB($id){
			try {
				$str = static::generateSQLDelete("id = ". $id);
				$str = $str . ModelTransDetail::generateSQLDelete('header_id = '. $id);
				DB::executeSQL($str);
			} catch (Exception $e) {
				$db->rollback();
				throw $e;
			}
		}

		public static function retrieve($id){
			$obj = parent::retrieve($id);
			if (isset($obj)) $obj->items = ModelTransDetail::retrieveList('header_id = '. $obj->id);
			return $obj;
		}
	}

	class ModelTransDetail extends BaseModel{
		public static function getFields(){
			return array(
				"header_id", "transdate", "header_flag", "warehouse_id", "item_id", "qty", "purchaseprice", "sellingprice"
			);
		}

		public static function saveToDB($obj, $db){
			try {
				static::saveObjToDB($obj, $db);
			} catch (Exception $e) {
				// $db->rollback();
				throw $e;
			}
		}


	}
