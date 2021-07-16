
singleton TSShapeConstructor(WaterReeds_01DAE)
{
   baseShape = "./waterReeds_01.DAE";
};

function WaterReeds_01DAE::onLoad(%this)
{
   %this.addImposter("64", "6", "0", "0", "512", "1", "0");
}
