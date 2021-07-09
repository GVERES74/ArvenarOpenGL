
singleton TSShapeConstructor(Banana02DAE)
{
   baseShape = "./Banana02.DAE";
   loadLights = "0";
};

function Banana02DAE::onLoad(%this)
{
   %this.addImposter("64", "6", "0", "0", "512", "1", "0");
}
