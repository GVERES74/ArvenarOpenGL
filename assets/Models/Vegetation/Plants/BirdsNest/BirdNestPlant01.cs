
singleton TSShapeConstructor(BirdNestPlant01DAE)
{
   baseShape = "./BirdNestPlant01.DAE";
   loadLights = "0";
};

function BirdNestPlant01DAE::onLoad(%this)
{
   %this.addImposter("64", "4", "4", "0", "512", "0", "4");
}
