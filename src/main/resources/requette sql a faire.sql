#issert d'instance de tache
INSERT INTO smctask_task_instance (FK_task_id) VALUE ('test');

#update complete d'instance de tache (check auto if all condition instance = true)
UPDATE smctask_task_instance SET smctask_task_instance.complete = true
 WHERE smctask_task_instance.id = 123456
 AND (SELECT COUNT(smctask_condition_instance.complete)
 FROM smctask_condition_instance WHERE smctask_condition_instance.FK_task_instance_id = 123456
 AND smctask_condition_instance.complete = false)  = 0;


#insert d'instance de condition
INSERT INTO smctask_condition_instance (FK_task_instance_id, FK_condition_condition_id, FK_task_id) VALUE (123456, 'break_stone_64', 'test');

#update quantity d'instance de condition 
UPDATE smctask_condition_instance SET quantity = 1
 WHERE FK_task_instance_id = 123456 AND FK_condition_condition_id = 'break_stone_64';

#update complete d'instance de condition (check auto if quantity is sup or equal to max quantity of the condition)
UPDATE smctask_condition_instance SET smctask_condition_instance.complete = true
 WHERE smctask_condition_instance.FK_task_instance_id = 123456
 AND smctask_condition_instance.FK_condition_condition_id = 'break_stone_64'
 AND smctask_condition_instance.quantity >= (SELECT smctask_condition.quantity
 FROM smctask_condition WHERE smctask_condition.condition_id = smctask_condition_instance.FK_condition_condition_id);

#insert de player jobs exp
INSERT INTO smctask_player_jobs_exp (uuid, FK_jobs_id) VALUE ('00000000-0000-0000-0000-000000000000', 'ami_des_elfe');

#update de player jobs exp (calcul auto level up with scale and reset exp with exp surplus when level up cant level up more than max level)
UPDATE smctask_player_jobs_exp SET smctask_player_jobs_exp.level = (CASE
 WHEN smctask_player_jobs_exp.level < (SELECT smctask_jobs.max_level FROM smctask_jobs
 WHERE smctask_jobs.id = smctask_player_jobs_exp.FK_jobs_id)
 AND exp >= (SELECT smctask_jobs.scale FROM smctask_jobs
 WHERE smctask_jobs.id = smctask_player_jobs_exp.FK_jobs_id) * smctask_player_jobs_exp.level * smctask_player_jobs_exp.level
 THEN level+1 ELSE smctask_player_jobs_exp.level END),
 smctask_player_jobs_exp.exp = (CASE
 WHEN smctask_player_jobs_exp.level < (SELECT smctask_jobs.max_level FROM smctask_jobs
 WHERE smctask_jobs.id = smctask_player_jobs_exp.FK_jobs_id)
 AND exp >= ((SELECT smctask_jobs.scale FROM smctask_jobs
 WHERE smctask_jobs.id = smctask_player_jobs_exp.FK_jobs_id) * smctask_player_jobs_exp.level * smctask_player_jobs_exp.level)
 THEN 0 + (smctask_player_jobs_exp.exp + 150) - ((SELECT smctask_jobs.scale FROM smctask_jobs
 WHERE smctask_jobs.id = smctask_player_jobs_exp.FK_jobs_id) * smctask_player_jobs_exp.level * smctask_player_jobs_exp.level)
 ELSE smctask_player_jobs_exp.exp + 150 END);


#get scale for user + jobs
SELECT (level * level * (SELECT scale FROM smctask_jobs WHERE id = 'alier_des_nain'))
 FROM smctask_player_jobs_exp WHERE uuid = '00000000-0000-0000-0000-000000000000' AND FK_jobs_id = 'alier_des_nain';

#update exp and level
UPDATE smctask_player_jobs_exp SET exp = exp + 100;
UPDATE smctask_player_jobs_exp SET exp = 0 + 100;
UPDATE smctask_player_jobs_exp SET level = level + 1;
 
 
#dump d'une tache et des donnée corespondante
DELETE FROM smctask_condition_instance WHERE FK_task_id = 'test';
DELETE FROM smctask_task_condition WHERE FK_task_id = 'test';
DELETE FROM smctask_task_description WHERE FK_task_id = 'test';
DELETE FROM smctask_task_instance WHERE FK_task_id = 'test';
DELETE FROM smctask_task_jobs_level WHERE FK_task_id = 'test';
DELETE FROM smctask_task_reward_command WHERE FK_task_id = 'test';
DELETE FROM smctask_task_reward_item WHERE FK_task_id = 'test';
DELETE FROM smctask_task_reward_jobs_exp WHERE FK_task_id = 'test';
DELETE FROM smctask_task WHERE id = 'test';
#test si la task est bien suprimer
SELECT id FROM smctask_task WHERE id = 'test';
#dump d'une condition et des donnée corespondante (seulement si aucune tache a cette condition)
DELETE FROM smctask_condition_instance WHERE FK_condition_condition_id = 'harvest_wheat_64'
 AND (SELECT count(FK_condition_condition_id) FROM smctask_task_condition WHERE FK_condition_condition_id = 'harvest_wheat_64') = 0;
DELETE FROM smctask_condition WHERE condition_id = 'harvest_wheat_64'
 AND (SELECT count(FK_condition_condition_id) FROM smctask_task_condition WHERE FK_condition_condition_id = 'harvest_wheat_64') = 0;
#test si la condition est bien suprimer
SELECT condition_id FROM smctask_condition WHERE condition_id = 'harvest_wheat_64';
#dump d'une rareter (seulement si aucune tache n'a cette rareter)
DELETE FROM smctask_rarity WHERE id = 'commun' AND (SELECT count(FK_rarity_id) FROM smctask_task WHERE FK_rarity_id = 'commun') = 0;
#test si la rareter est bien suprimer
SELECT id FROM smctask_rarity WHERE id = 'commun';
#dump d'un jobs et des donnée corespondante (seulement si aucune tache n'a ce jobs en condition d'obtention ou récompence d'exp)
DELETE FROM smctask_player_jobs_exp WHERE FK_jobs_id = 'ami_des_elfe' AND
 (SELECT count(FK_jobs_id) FROM smctask_task_jobs_level WHERE FK_jobs_id = 'ami_des_elfe') = 0 AND
 (SELECT count(FK_jobs_id) FROM smctask_task_reward_jobs_exp WHERE FK_jobs_id = 'ami_des_elfe') = 0;
DELETE FROM smctask_jobs WHERE id = 'ami_des_elfe' AND
 (SELECT count(FK_jobs_id) FROM smctask_task_jobs_level WHERE FK_jobs_id = 'ami_des_elfe') = 0 AND
 (SELECT count(FK_jobs_id) FROM smctask_task_reward_jobs_exp WHERE FK_jobs_id = 'ami_des_elfe') = 0;
#test si le jobs est bien suprimer
SELECT id FROM smctask_jobs WHERE id = 'ami_des_elfe';


#update d'une tache (/!\ supression de donnée utilisateur si par exemple les condition change les instance de condition qui disparaisse sont suprimée)
#update d'une condition
#update d'une rareter
#update d'un jobs

#select for task in task inventory
#select for task in task panel